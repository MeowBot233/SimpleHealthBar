package yukisawanya.simplehealthbar.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class NameTagRendering {
    @Final @Shadow protected EntityRenderDispatcher dispatcher;
    @Shadow public abstract TextRenderer getTextRenderer();
    @Shadow protected abstract boolean hasLabel(Entity entity);
    //@Shadow protected abstract void renderLabelIfPresent(Entity entity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light);

    @Shadow @Final private TextRenderer textRenderer;

    @Inject(method = "render", at = @At("HEAD"))
    private void render(Entity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        //If HUD is hidden
        if (MinecraftClient.getInstance().options.hudHidden) return;
        if (dispatcher.targetedEntity != entity) return;
        int y;
        if(hasLabel(entity)) y = -10; else y = 0;
        renderLabel(y, entity, matrices, vertexConsumers, light);
    }

    protected void renderLabel(int y, Entity entity, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        double d = this.dispatcher.getSquaredDistanceToCamera(entity);
        if (d <=4096.0D && entity instanceof LivingEntity) {
            float percent = ((LivingEntity) entity).getHealth() / ((LivingEntity) entity).getMaxHealth();
            String health = "§c❤ ";
            if(percent < 0.3) health += String.format("§c%.1f", ((LivingEntity) entity).getHealth());
            else if(percent < 0.7) health += String.format("§6%.1f", ((LivingEntity) entity).getHealth());
            else health += String.format("§a%.1f", ((LivingEntity) entity).getHealth());
            health += "§r/§a" + ((LivingEntity) entity).getMaxHealth();
            Text text = new LiteralText(health);
            boolean bl = !entity.isSneaky();
            float f = entity.getHeight() + 0.5F;
            int i = "deadmau5".equals(entity.getDisplayName().asString()) ? y-10 : y;
            matrices.push();
            matrices.translate(0.0D, f, 0.0D);
            matrices.multiply(this.dispatcher.getRotation());
            matrices.scale(-0.025F, -0.025F, 0.025F);
            Matrix4f matrix4f = matrices.peek().getPositionMatrix();
            float g = MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25F);
            int j = (int) (g * 255.0F) << 24;
            TextRenderer textRenderer = this.getTextRenderer();
            float h = (float) (-textRenderer.getWidth(text) / 2);
            textRenderer.draw(text, h, (float) i, 553648127, false, matrix4f, vertexConsumers, bl, j, light);
            if (bl) {
                textRenderer.draw(text, h, (float) i, -1, false, matrix4f, vertexConsumers, false, 0, light);
            }

            matrices.pop();
        }
    }
}
